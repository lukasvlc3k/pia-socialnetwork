import React from 'react';
import styles from '../../styles/post.module.scss';
import { PostDto, PostDtoPostTypeEnum } from '../../api';
import moment from 'moment';
import sanitizeHtml from 'sanitize-html';
import { allowedHtmlTagsInPosts } from '../../consts/general';

type PostProps = {
    post: PostDto;
};
export default function Post(props: PostProps) {
    function sanitizedContent() {
        let preprocessing: string = props.post?.content ?? '';
        preprocessing = preprocessing?.replaceAll('\n', '<br />');

        return sanitizeHtml(preprocessing, {
            allowedTags: allowedHtmlTagsInPosts,
        });
    }
    return (
        <div className={styles.wrapperPost}>
            <div
                className={styles.header}
                style={
                    props.post.postType === PostDtoPostTypeEnum.Announcement
                        ? { backgroundColor: '#C03221' }
                        : {}
                }
            >
                <div className={styles.user}>{props.post.user?.name}</div>
                <div
                    className={styles.date}
                    title={moment(props.post.publishedDate).format('DD.MM.yyyy HH:mm:ss')}
                >
                    {moment(props.post.publishedDate).fromNow()}
                </div>
            </div>
            <div dangerouslySetInnerHTML={{ __html: sanitizedContent() }} />
        </div>
    );
}
