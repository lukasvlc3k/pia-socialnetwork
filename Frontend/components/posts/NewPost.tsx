import React, { useState } from 'react';
import { FloatingLabel, Form } from 'react-bootstrap';
import Button from 'react-bootstrap/Button';
import styles from '../../styles/post.module.scss';
import Auth from '../common/Auth';
import { RoleNameEnum } from '../../api';

type NewPostProps = {
    onCreate: (content: string, asAnnouncement: boolean) => Promise<boolean>;
};
export default function NewPost(props: NewPostProps) {
    const [content, setContent] = useState('');
    const [asAnnouncement, setAsAnnouncement] = useState(false);

    async function submitPost() {
        const res = await props.onCreate(content, asAnnouncement);
        if (res) {
            // success
            setContent('');
        }
    }
    return (
        <div className={styles.wrapperNewPost}>
            <h2>Nový příspěvek</h2>
            <Form
                onSubmit={async (e) => {
                    e.preventDefault();
                    await submitPost();
                }}
            >
                <FloatingLabel controlId="formContent" label="Text příspěvku">
                    <Form.Control
                        as="textarea"
                        style={{ height: '150px' }}
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        className={styles.textarea}
                        required={true}
                    />
                </FloatingLabel>

                <div>
                    <Button variant="primary" type="submit">
                        Zveřejnit
                    </Button>
                    <Auth minRole={RoleNameEnum.Admin}>
                        <label>
                            <input
                                type="checkbox"
                                name={'asAnnouncement'}
                                checked={asAnnouncement}
                                onChange={(e) => setAsAnnouncement(e.target.checked)}
                                style={{ marginLeft: '5px', marginRight: '5px' }}
                            />
                            Zveřejnit jako oznámení
                        </label>
                    </Auth>
                </div>
            </Form>
        </div>
    );
}
