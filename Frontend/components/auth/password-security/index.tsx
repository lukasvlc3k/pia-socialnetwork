import React, { useEffect, useState } from 'react';
import { Form, ProgressBar } from 'react-bootstrap';
import { setSearchParams } from '../../../api/common';
import { PasswordSecurityLevels } from '../../../consts/general';

type PasswordSecurityProps = {
    password: string;
    setPassword: (password: string) => any;

    security: number;
    setSecurity: (security: number) => any;

    required?: boolean;
};

export default function PasswordSecurityTextbox(props: PasswordSecurityProps) {
    const [variant, setVariant] = useState('danger');
    const [label, setLabel] = useState('');

    function computeSecurity(pw: string) {
        return pw.length;
    }

    function onChange(newPassword: string) {
        refreshSecurity(newPassword);
        props.setPassword(newPassword);
    }

    function securityBarPercent() {
        const max = PasswordSecurityLevels[PasswordSecurityLevels.length - 1];
        if (!max || max === 0) {
            return 100;
        } else {
            const value = (props.security / max) * 100;
            return Math.min(value, 100);
        }
    }
    function refreshSecurity(newPassword: string) {
        const newSecurity = computeSecurity(newPassword);
        props.setSecurity(newSecurity);

        if (PasswordSecurityLevels.length !== 3) {
            return 'dark'; // invalid levels
        }

        const min = PasswordSecurityLevels[0];
        const strong = PasswordSecurityLevels[1];
        const max = PasswordSecurityLevels[2];

        if (newSecurity < min) {
            setLabel('Slabé');
            setVariant('danger');
        } else if (newSecurity < strong) {
            setLabel('Dostatečné');
            setVariant('warning');
        } else {
            setLabel('Silné');
            setVariant('success');
        }
    }

    return (
        <div>
            <Form.Control
                type="password"
                placeholder="Heslo"
                value={props.password}
                required={props.required}
                onChange={(e) => onChange(e.target.value)}
            />
            <ProgressBar variant={variant} now={securityBarPercent()} label={label} />
        </div>
    );
}
