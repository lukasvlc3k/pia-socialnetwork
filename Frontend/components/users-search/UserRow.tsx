import React from 'react';
import { PostDto, PostDtoPostTypeEnum, UserDto } from '../../api';
import moment from 'moment';
import sanitizeHtml from 'sanitize-html';
import { allowedHtmlTagsInPosts } from '../../consts/general';
import styles from '../../styles/search.module.scss';
import Button from 'react-bootstrap/Button';

type UserRowProps = {
    user: UserDto;
};
export default function UserRow(props: UserRowProps) {
    return (
        <div className={styles.userWrapper}>
            <div>{props.user.name}</div>
            <div>{props.user.email}</div>
            <div>
                <Button>Poslat žádost o přátelství</Button>
            </div>
        </div>
    );
}
