import React, { useState } from 'react';
import { userController } from '../../controllers';
import { UserDto } from '../../api';
import styles from '../../styles/search.module.scss';
import { Form } from 'react-bootstrap';
import UserRow from './UserRow';

type UsersSearchProps = {
    onSendFriendRequest: (userId: number) => Promise<boolean>;

    searchFor: string;
    setSearchFor: (newSearchFor: string) => void;

    searchResults: UserDto[] | null;
    setSearchResults: (newResults: UserDto[] | null) => void;

    doSearch: (value: string) => Promise<void>;
};

export default function UsersSearch(props: UsersSearchProps) {
    async function onSearchChanged(newValue: string) {
        props.setSearchFor(newValue);

        if (newValue.length >= 3) {
            await props.doSearch(newValue);
        } else {
            props.setSearchResults(null);
        }
    }

    function flexResults(count: number) {
        if (count === 1) return 'Byl nalezen 1 uživatel';
        else if (count > 1 && count < 5) return 'Byli nalezeni ' + count + ' uživatelé';
        else return 'Bylo nalezeno ' + count + ' uživatelů';
    }

    return (
        <div className={'container'}>
            <Form.Control
                className={styles.searchBox}
                type="text"
                placeholder="Hledaný uživatel"
                required
                value={props.searchFor}
                onChange={(e) => onSearchChanged(e.target.value)}
            />

            <div className={styles.searchResultsSummary}>
                {props.searchResults == null && (
                    <p>Pro zobrazení výsledků hledání je potřeba zadat nejméně 3 znaky</p>
                )}
                {props.searchResults != null && props.searchResults.length === 0 && (
                    <p>
                        Nebyly nalezeni žádní uživatelé odpovídající vyhledávání &apos;
                        {props.searchFor}&apos;
                    </p>
                )}
                {props.searchResults != null && props.searchResults.length > 0 && (
                    <p>{flexResults(props.searchResults.length)}</p>
                )}
            </div>
            {props.searchResults?.map((r) => (
                <UserRow
                    key={r.id}
                    user={r}
                    onSendFriendRequest={props.onSendFriendRequest}
                />
            ))}
        </div>
    );
}
