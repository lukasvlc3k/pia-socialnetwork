import React, { useContext, useState } from 'react';
import type { NextPage } from 'next';
import Head from 'next/head';
import styles from '../styles/login.module.css';
import Button from 'react-bootstrap/Button';
import Link from 'next/link';
import { Alert, Form } from 'react-bootstrap';
import { useRouter } from 'next/router';
import { LoggedUserContext } from '../contexts/LoggedUserContext';

const Login: NextPage = () => {
    const router = useRouter();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [error, setError] = useState('');
    const { login } = useContext(LoggedUserContext);

    async function onLogin() {
        const loginRes = await login(email, password);

        if (!loginRes) {
            setError('Neplatné přihlašovací údaje');
        } else {
            setError('');
            await router.push('/app');
        }
    }

    return (
        <div className={styles.layoutwrapper}>
            <Head>
                <title>Login | PIA Network</title>
                <meta
                    name="description"
                    content="Social network as a semestral work for KIV/PIA"
                />
            </Head>

            <div className={styles.header}>PIA NETWORK</div>
            <main className={styles.loginWrapper}>
                <div className={styles.login}>
                    {error && <Alert variant={'danger'}>{error}</Alert>}

                    <h1>Přihlášení</h1>

                    <Form
                        onSubmit={async (e) => {
                            e.preventDefault();
                            await onLogin();
                        }}
                    >
                        <Form.Group className="mb-3" controlId="formBasicEmail">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="Email"
                                required
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="formBasicPassword">
                            <Form.Label>Heslo</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Heslo"
                                required
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit">
                            Přihlásit se
                        </Button>
                    </Form>

                    <div className={styles.registrationLinkWrapper}>
                        <Link href="/registration">
                            <a>Zaregistrovat nový účet</a>
                        </Link>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default Login;
