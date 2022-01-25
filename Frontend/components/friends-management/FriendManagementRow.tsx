import { RoleNameEnum, UserDto, UserDtoRolesEnum } from '../../api';
import React, { useContext } from 'react';
import styles from '../../styles/friends.module.scss';
import Auth from '../common/Auth';
import Button from 'react-bootstrap/Button';
import { LoggedUserContext } from '../../contexts/LoggedUserContext';

interface FriendRowProps {
    friend: UserDto;
    onSetAdmin: (isAdmin: boolean) => Promise<void>;
}
export default function FriendManagementRow(props: FriendRowProps) {
    const { loggedUser } = useContext(LoggedUserContext);

    function isAdmin() {
        return props.friend.roles?.includes(UserDtoRolesEnum.Admin);
    }

    function AdminButton() {
        if (props.friend.id === loggedUser?.id) {
            return null;
        }

        return (
            <Button
                onClick={async () => {
                    props.onSetAdmin(!isAdmin());
                }}
                variant={isAdmin() ? 'danger' : 'success'}
            >
                {isAdmin() ? 'Odebrat admin práva' : 'Přidat admin práva'}
            </Button>
        );
    }
    return (
        <div title={props.friend.email} className={styles.friendRowManagement}>
            <div className={styles.name}>{props.friend.name}</div>
            <div className={styles.email}>{props.friend.email}</div>
            <Auth minRole={RoleNameEnum.Admin}>
                <div className={styles.actions}>
                    <AdminButton />
                </div>
            </Auth>
        </div>
    );
}
