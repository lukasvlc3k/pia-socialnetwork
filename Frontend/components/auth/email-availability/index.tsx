import React, { useEffect, useState } from 'react';
import { Badge, Form, ProgressBar } from 'react-bootstrap';
import { setSearchParams } from '../../../api/common';
import { PasswordSecurityLevels } from '../../../consts/general';
import { publicController } from '../../../controllers';

type EmailAvailabilityProps = {
    email: string;
    setEmail: (password: string) => any;

    isAvailable: boolean | null;
    setIsAvailable: (available: boolean | null) => any;
    required?: boolean;

    isValidEmail: (email: string) => boolean;
};

export default function EmailAvailabilityTextbox(props: EmailAvailabilityProps) {
    const [valid, setValid] = useState<boolean | null>(null);

    async function onChange(newEmail: string) {
        props.setEmail(newEmail);

        if (newEmail === '') {
            setValid(null);
            props.setIsAvailable(null);
            return;
        }

        const valid = props.isValidEmail(newEmail);
        setValid(valid);

        if (valid) {
            const res = await publicController.emailAvailable(newEmail);
            const available = res.data.response ?? false;

            props.setIsAvailable(available);
        } else {
            props.setIsAvailable(null);
        }
    }

    return (
        <div>
            <Form.Control
                type="email"
                placeholder="Email"
                required={props.required}
                value={props.email}
                onChange={(e) => onChange(e.target.value)}
            />
            {valid === false && <Badge bg="danger">Neplatný email</Badge>}
            {props.isAvailable === false && (
                <Badge bg="danger">Tento email je již použitý</Badge>
            )}
            {props.isAvailable === true && (
                <Badge bg="success">Tento email je volný</Badge>
            )}
        </div>
    );
}
