import React, { useEffect, useState } from 'react';
import NewPost from './NewPost';
import { postController } from '../../controllers';
import { PostCreateRequestPostTypeEnum, PostDto } from '../../api';
import { ShowToast } from '../../utils/alerts';
import Post from './Post';

export default function Posts() {
    const [posts, setPosts] = useState<PostDto[]>([]);

    useEffect(() => {
        firstLoadPosts();
    }, []);

    async function onCreate(content: string): Promise<boolean> {
        if (content.length === 0) {
            ShowToast('Není možné zveřejnit prázdný příspěvek', 'error', 3000, 'top');
            return false;
        }
        try {
            const res = await postController.createPost({
                content: content,
                postType: PostCreateRequestPostTypeEnum.Post,
            });
            ShowToast('Příspěvek byl úspěšně zveřejněn', 'success', 3000, 'top');
            return true;
        } catch (e) {
            console.log(e);
        }

        return false;
    }

    async function firstLoadPosts() {
        console.log('loading all posts');
        const res = await postController.getPosts('10');
        console.log(res);

        if (res) {
            setPosts(res.data);
        }
    }

    return (
        <div>
            <NewPost onCreate={onCreate} />

            {posts?.map((p) => (
                <Post key={p.postId} post={p} />
            ))}
        </div>
    );
}
