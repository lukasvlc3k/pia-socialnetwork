import React, { useState } from 'react';
import { userController } from '../../controllers';
import { UserDto } from '../../api';
import styles from '../../styles/search.module.scss';
import { Form } from 'react-bootstrap';
import UserRow from './UserRow';

export default function UsersSearch() {
    const [searchFor, setSearchFor] = useState('');
    const [searchResults, setSearchResults] = useState<UserDto[] | null>(null);

    async function onSearchChanged(newValue: string) {
        setSearchFor(newValue);

        if (newValue.length >= 3) {
            doSearch(newValue);
        } else {
            setSearchResults(null);
        }
    }
    async function doSearch(value: string) {
        try {
            const res = await userController.searchRelevantUsers(value);
            console.log(res);
            setSearchResults(res.data);
        } catch (e) {
            console.log(e);
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
                value={searchFor}
                onChange={(e) => onSearchChanged(e.target.value)}
            />

            <div className={styles.searchResultsSummary}>
                {searchResults == null && (
                    <p>Pro zobrazení výsledků hledání je potřeba zadat nejméně 3 znaky</p>
                )}
                {searchResults != null && searchResults.length === 0 && (
                    <p>
                        Nebyly nalezeni žádní uživatelé odpovídající vyhledávání &apos;
                        {searchFor}&apos;
                    </p>
                )}
                {searchResults != null && searchResults.length > 0 && (
                    <p>{flexResults(searchResults.length)}</p>
                )}
            </div>
            {searchResults?.map((r) => (
                <UserRow key={r.id} user={r} />
            ))}
        </div>
    );
}
