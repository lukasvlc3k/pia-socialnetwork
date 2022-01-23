import React from 'react';
import type { NextPage } from 'next';
import Head from 'next/head';
import { useState } from 'react';
import styles from '../styles/login.module.css';
import Button from 'react-bootstrap/Button';
import Link from 'next/link';
import { authController } from '../controllers';
import { Form } from 'react-bootstrap';

const Login: NextPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    async function login() {
        const res = await authController.loginUser({ email, password });
        console.log(res);
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
            <main className={styles.login}>
                <h1>Přihlášení</h1>

                <Form
                    onSubmit={async (e) => {
                        e.preventDefault();
                        await login();
                    }}
                >
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>Email</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBasicPassword">
                        <Form.Label>Heslo</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Heslo"
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
            </main>
        </div>
    );
};

export default Login;
