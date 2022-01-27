import React, { useEffect, useState } from 'react';
import { Form, ProgressBar } from 'react-bootstrap';
import { setSearchParams } from '../../../api/common';
import { PasswordSecurityLevels } from '../../../consts/general';
import { number } from 'prop-types';

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
        const n: number = pw.length;
        let hasLower = false;
        let hasUpper = false;
        let hasDigit = false;
        let hasSpecialChar = false;

        for (let i = 0; i < pw.length; i++) {
            const char = pw.charAt(i);
            if (char === char.toLowerCase()) {
                hasLower = true;
            } else if (char === char.toUpperCase()) {
                hasUpper = true;
            } else if (char >= '0' && char <= '9') {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }
        }

        let possibleSymbols = 0;
        if (hasLower) {
            possibleSymbols += 26;
        }
        if (hasUpper) {
            possibleSymbols += 26;
        }
        if (hasDigit) {
            possibleSymbols += 10;
        }
        if (hasSpecialChar) {
            possibleSymbols += 33;
        }

        const totalCombinations: number = Math.pow(possibleSymbols, n);
        const bitsOfEntropy: number = Math.log2(totalCombinations);

        console.log(totalCombinations);
        console.log(bitsOfEntropy);
        return bitsOfEntropy;
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
