import React, { useEffect, useState } from 'react';
import { friendController } from '../../controllers';
import { UserDto } from '../../api';
import FriendManagementRow from './FriendManagementRow';

interface FriendManagementProps {
    friends: UserDto[] | null;
    onSetAdmin: (userId: number | null, isAdmin: boolean) => Promise<void>;
}
export default function FriendManagement(props: FriendManagementProps) {
    return (
        <div className={'container'}>
            <h2>Přátelé</h2>

            {props.friends?.map((f) => (
                <FriendManagementRow
                    friend={f}
                    key={f.id}
                    onSetAdmin={async (isAdmin) => {
                        props.onSetAdmin(f.id ?? null, isAdmin);
                    }}
                />
            ))}
        </div>
    );
}
