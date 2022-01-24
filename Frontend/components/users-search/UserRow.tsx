import React from 'react';
import { UserDto, UserDtoCanBeAddedToFriendsTypeEnum } from '../../api';
import styles from '../../styles/search.module.scss';
import Button from 'react-bootstrap/Button';

type UserRowProps = {
    user: UserDto;
    onSendFriendRequest: (userId: number) => Promise<boolean>;
};
export default function UserRow(props: UserRowProps) {
    async function sendFriendRequest() {
        if (!props.user?.id) {
            return;
        }

        await props.onSendFriendRequest(props.user.id);
    }

    function RequestPart(rprops: {
        canBeAddedToFriendsType: UserDtoCanBeAddedToFriendsTypeEnum;
    }) {
        switch (rprops.canBeAddedToFriendsType) {
            case UserDtoCanBeAddedToFriendsTypeEnum.Yes:
                return (
                    <Button onClick={sendFriendRequest}>
                        Poslat žádost o přátelství
                    </Button>
                );
            case UserDtoCanBeAddedToFriendsTypeEnum.NoBlocked:
                return <i>Tohoto uživatele máte zablokovaného</i>;
            case UserDtoCanBeAddedToFriendsTypeEnum.NoAlreadyFriend:
                return <i>Tohoto uživatele již máte v přátelích</i>;
            case UserDtoCanBeAddedToFriendsTypeEnum.NoFriendRequestPending:
                return <i>Od tohoto uživatele máte žádost o přátelství</i>;
            case UserDtoCanBeAddedToFriendsTypeEnum.NoFriendRequestSent:
                return <i>Žádost již byla odeslána</i>;
            case UserDtoCanBeAddedToFriendsTypeEnum.NoMe:
                return <i>Nemůžete přidat do přátel sám sebe</i>;
        }
    }

    return (
        <div className={styles.userWrapper}>
            <div>{props.user.name}</div>
            <div>{props.user.email}</div>
            <div>
                <RequestPart
                    canBeAddedToFriendsType={
                        props.user.canBeAddedToFriendsType ??
                        UserDtoCanBeAddedToFriendsTypeEnum.Yes
                    }
                />
            </div>
        </div>
    );
}
