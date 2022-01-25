import React, { useContext, useRef, useState } from 'react';
import Head from 'next/head';
import styles from '../styles/login.module.css';
import Button from 'react-bootstrap/Button';
import Link from 'next/link';
import { authController } from '../controllers';
import { Alert, Form } from 'react-bootstrap';
import PasswordSecurityTextbox from '../components/auth/password-security';
import { PasswordSecurityLevels } from '../consts/general';
import EmailAvailabilityTextbox from '../components/auth/email-availability';
import { SignupResponseStateEnum } from '../api';
import { useRouter } from 'next/router';
import { LoggedUserContext } from '../contexts/LoggedUserContext';

export default function Registration() {
    const router = useRouter();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordConfirmation, setPasswordConfirmation] = useState('');
    const [name, setName] = useState('');

    const [emailAvailable, setEmailAvailable] = useState<boolean | null>(null);
    const [passwordSecurity, setPasswordSecurity] = useState<number>(0);

    const [error, setError] = useState('');
    const [ok, setOk] = useState(false);

    const formRef = useRef(null);
    const { login } = useContext(LoggedUserContext);

    function validateEmail(em: string): boolean {
        const regex = new RegExp(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );

        return regex.test(em);
    }

    function validateForm(): boolean {
        if (!validateEmail(email)) {
            setError('Neplatný email');
            return false;
        }

        if (password !== passwordConfirmation) {
            setError('Hesla se neshodují');
            return false;
        }

        const minPasswordLevel = PasswordSecurityLevels[0] ?? 0;
        if (passwordSecurity < minPasswordLevel) {
            setError('Hesla není dostatečně silné');
            return false;
        }

        return true;
    }

    function resetForm() {
        setPassword('');
        setPasswordConfirmation('');
        setPasswordSecurity(0);
        setEmail('');
        setEmailAvailable(null);
        setName('');
    }

    async function handleOk() {
        setError('');
        setOk(true);

        const e = email;
        const pw = password;

        resetForm();

        setTimeout(async () => {
            const res = await login(e, pw);
            if (res) {
                await router.push('/app');
            }
        }, 3000);
    }

    function handleInvalidEmail() {
        setError('Neplatný email.');
        setOk(false);
    }

    function handleEmailInUse() {
        setError('Tento email je již zaregistrován. Zadejte jiný.');
        setOk(false);
    }

    function handleInvalidName() {
        setError('Zadejte platné jméno a příjmení.');
        setOk(false);
    }

    function handleWeakPassword() {
        setError(
            'Vaše heslo je příliš slabé. Zkuste přidat velká a malá písmena, čísla nebo speciální znaky'
        );
        setOk(false);
    }

    async function signup() {
        const validationRes = validateForm();

        if (!validationRes) {
            return;
        }

        try {
            const res = await authController.registerUser({ email, name, password });
            switch (res.data.state) {
                case SignupResponseStateEnum.Ok:
                    await handleOk();
                    break;
                case SignupResponseStateEnum.EmailInUse:
                    handleEmailInUse();
                    break;
                case SignupResponseStateEnum.InvalidEmail:
                    handleInvalidEmail();
                    break;
                case SignupResponseStateEnum.WeakPassword:
                    handleWeakPassword();
                    break;
                case SignupResponseStateEnum.InvalidName:
                    handleInvalidName();
                    break;
            }
        } catch (e) {
            setError('Došlo k chybě. Zkuste to prosím znovu později');
            console.log(e);
        }
    }

    return (
        <div className={styles.layoutwrapper}>
            <Head>
                <title>Registrace | PIA Network</title>
                <meta
                    name="description"
                    content="Social network as a semestral work for KIV/PIA"
                />
            </Head>

            <div className={styles.header}>PIA NETWORK</div>
            <main className={styles.loginWrapper}>
                <div className={styles.login}>
                    {error && <Alert variant={'danger'}>{error}</Alert>}
                    {ok && (
                        <Alert variant={'success'}>
                            Registrace byla úspěšně dokončena. Za 3 sekundy budete
                            přesměrován do aplikace
                        </Alert>
                    )}

                    <h1>Registrace</h1>

                    <Form
                        onSubmit={async (e) => {
                            e.preventDefault();
                            await signup();
                        }}
                        ref={formRef}
                    >
                        <Form.Group className="mb-3" controlId="formBasicName">
                            <Form.Label>Jméno a příjmení</Form.Label>
                            <Form.Control
                                type="text"
                                required
                                placeholder="Jméno a příjmení"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="formBasicEmail">
                            <Form.Label>Email</Form.Label>
                            <EmailAvailabilityTextbox
                                email={email}
                                setEmail={setEmail}
                                isAvailable={emailAvailable}
                                setIsAvailable={setEmailAvailable}
                                isValidEmail={validateEmail}
                                required={true}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="formBasicPassword">
                            <Form.Label>Heslo</Form.Label>
                            <PasswordSecurityTextbox
                                password={password}
                                required
                                setPassword={setPassword}
                                security={passwordSecurity}
                                setSecurity={setPasswordSecurity}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="formBasicPasswordConfirm">
                            <Form.Label>Heslo pro kontrolu znovu</Form.Label>
                            <Form.Control
                                type="password"
                                required
                                placeholder="Heslo"
                                value={passwordConfirmation}
                                onChange={(e) => setPasswordConfirmation(e.target.value)}
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit">
                            Zaregistrovat se
                        </Button>
                    </Form>

                    <div className={styles.registrationLinkWrapper}>
                        <Link href="/">
                            <a>&lt;- Zpět na přihlášení</a>
                        </Link>
                    </div>
                </div>
            </main>
        </div>
    );
}
