import React, { useEffect, useRef, useState } from 'react';
import NewPost from './NewPost';
import { postController } from '../../controllers';
import { PostCreateRequestPostTypeEnum, PostDto } from '../../api';
import { ShowToast } from '../../utils/alerts';
import Post from './Post';
import { maxPostLength, postsRefreshInterval } from '../../consts/general';
import styles from '../../styles/main-layout.module.scss';

export default function Posts() {
    const [posts, setPosts] = useState<PostDto[]>([]);
    const listInnerRef = useRef();
    let isLoading = false;

    useEffect(() => {
        firstLoadPosts();
    }, []);

    useEffect(() => {
        const interval = setInterval(async () => {
            await loadNewPosts();
        }, postsRefreshInterval);
        return () => clearInterval(interval);
    }, [posts]);

    async function onScroll() {
        if (listInnerRef.current) {
            const { scrollTop, scrollHeight, clientHeight } = listInnerRef.current;
            if (scrollTop + clientHeight >= scrollHeight - window.innerHeight / 4) {
                await onScrollBottomReached();
            }
        }
    }

    async function onScrollBottomReached() {
        if (isLoading) {
            return;
        }

        await loadOldPosts();
    }

    async function onCreate(content: string, asAnnouncement: boolean): Promise<boolean> {
        if (content.length === 0) {
            ShowToast('Není možné zveřejnit prázdný příspěvek', 'error', 3000, 'top');
            return false;
        }
        if (content.length > maxPostLength) {
            ShowToast('Maximální délka příspěvku je 8000 znaků.', 'error', 3000, 'top');
            return false;
        }

        try {
            const res = await postController.createPost({
                content: content,
                postType: asAnnouncement
                    ? PostCreateRequestPostTypeEnum.Announcement
                    : PostCreateRequestPostTypeEnum.Post,
            });
            if (res.data.ok) {
                ShowToast('Příspěvek byl úspěšně zveřejněn', 'success', 3000, 'top');
                await loadNewPosts();
                return true;
            } else {
                ShowToast('Došlo k chybě při odesílání příspěvku.', 'error', 3000, 'top');
                console.log(res);
                return false;
            }
        } catch (e) {
            console.log(e);
        }

        return false;
    }

    function hasPostsChanged(newPosts: PostDto[]) {
        if (newPosts.length !== posts.length) {
            return true;
        }

        // same length
        for (let i = 0; i < newPosts.length; i++) {
            if (newPosts[i].postId !== posts[i].postId) {
                return true;
            }
        }

        return false;
    }

    async function firstLoadPosts() {
        if (isLoading) return;

        try {
            isLoading = true;
            const res = await postController.getPosts('10');

            if (res) {
                if (hasPostsChanged(res.data)) {
                    setPosts(res.data);
                }
            }
        } catch (e) {
            console.log(e);
        } finally {
            isLoading = false;
        }
    }

    async function loadNewPosts() {
        if (isLoading) return;

        try {
            isLoading = true;
            const first = posts.length > 0 ? posts[0] : null;

            const res = await postController.getPosts(
                undefined,
                first?.publishedDate ?? undefined
            );
            if (res) {
                if (res.data.length > 0) {
                    console.log(res.data.length, 'new posts found');
                    setPosts([...res.data, ...posts]);
                }
            }
        } catch (e) {
            console.log(e);
        } finally {
            isLoading = false;
        }
    }

    async function loadOldPosts() {
        if (isLoading) return;

        try {
            isLoading = true;
            const last = posts.length > 0 ? posts[posts.length - 1] : null;

            const res = await postController.getPosts(
                '3',
                undefined,
                last?.publishedDate ?? undefined
            );
            if (res) {
                if (res.data.length > 0) {
                    console.log(res.data.length, 'old posts found');
                    setPosts([...posts, ...res.data]);
                }
            }
        } catch (e) {
            console.log(e);
        } finally {
            isLoading = false;
        }
    }

    return (
        // @ts-ignore
        <div className={styles.posts} onScroll={onScroll} ref={listInnerRef}>
            <div className={styles.postsContainer}>
                <div>
                    <NewPost onCreate={onCreate} />

                    {posts?.map((p) => (
                        <Post key={p.postId} post={p} />
                    ))}
                </div>
            </div>
        </div>
    );
}
