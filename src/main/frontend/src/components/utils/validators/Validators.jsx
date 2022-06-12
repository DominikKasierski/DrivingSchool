import React from 'react';
import i18n from "../../../i18n";

const sizeValidator = (data, minLength, maxLength) => {
    if (data.length < minLength) {
        return "form.validation.field.min.size";
    }
    if (data.length > maxLength) {
        return "form.validation.field.max.size";
    }
}

const patternValidator = (data, pattern) => {
    if (!pattern.test(data)) {
        return "form.validation.field.pattern";
    }
}

const enumValidator = (data, list) => {
    if (!list.includes(data)) {
        return "form.validation.field.not.allowed";
    }
}

const validateLogin = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 3, 20));
    errors.push(patternValidator(data, /^[a-zA-Z0-9]+$/));
    return errors.filter(err => err !== undefined);
}

const validateEmailAddress = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 6, 127));
    errors.push(patternValidator(data, /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/));
    return errors.filter(err => err !== undefined);
}

const validateFirstname = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 3, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[a-ząęćńóśłźż]+$/));
    return errors.filter(err => err !== undefined);
}

const validateLastname = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[a-ząęćńóśłźż]+$/));
    return errors.filter(err => err !== undefined);
}

const validatePassword = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 8, 64));
    errors.push(patternValidator(data, /^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,64}$/));
    return errors.filter(err => err !== undefined);
}

const validatePhoneNumber = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 9, 15));
    errors.push(patternValidator(data, /^[0-9+][0-9]{8,14}$/));
    return errors.filter(err => err !== undefined);
}

const validateConfirmationCode = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 32, 32));
    errors.push(patternValidator(data, /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/));
    return errors.filter(err => err !== undefined);
}

const validateLanguage = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 2));
    errors.push(patternValidator(data, /^[a-z]{2}$/));
    errors.push(enumValidator(data, ["pl", "en"]));
    return errors.filter(err => err !== undefined);
}

const validateImage = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 5, 31));
    errors.push(patternValidator(data, /^(?!\/\/)(\/[A-Za-z0-9]+)+\.(jpg|png)$/));
    return errors.filter(err => err !== undefined);
}

const validateComment = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 4, 255));
    return errors.filter(err => err !== undefined);
}

const validatePrice = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 8));
    errors.push(patternValidator(data, /^(\d*\.)?\d+$/));
    return errors.filter(err => err !== undefined);
}

const validateRegistrationNumber = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 4, 7));
    errors.push(patternValidator(data, /^[A-Z0-9]{4,7}$/));
    return errors.filter(err => err !== undefined);
}

const changeValidationMessages = (errors, identity) => {
    return errors.map(x => {
        const translationString = x.replace("field", "field." + identity.toLowerCase());
        const translated = i18n.t(translationString);
        return translated === translationString ? i18n.t(x) : translated;
    });
}

