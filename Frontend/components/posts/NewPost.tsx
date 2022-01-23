import React, { useEffect, useState } from 'react';
import { FloatingLabel, Form, ProgressBar } from 'react-bootstrap';
import Button from 'react-bootstrap/Button';
import styles from '../../styles/post.module.scss';

type NewPostProps = {
    onCreate: (content: string) => Promise<boolean>;
};
export default function NewPost(props: NewPostProps) {
    const [content, setContent] = useState('');

    async function submitPost() {
        const res = await props.onCreate(content);
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

                <Button variant="primary" type="submit">
                    Zveřejnit
                </Button>
            </Form>
        </div>
    );
}
