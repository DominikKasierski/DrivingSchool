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
    errors.push(sizeValidator(data, 4, 50));
    return errors.filter(err => err !== undefined);
}

const validatePrice = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 4));
    errors.push(patternValidator(data, /^(\d*\.)?\d+$/));
    return errors.filter(err => err !== undefined);
}

const validateCourseCategory = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 1));
    errors.push(patternValidator(data, /^[A-F]$/));
    errors.push(enumValidator(data, ["A", "B", "C"]));
    return errors.filter(err => err !== undefined);
}

const validateRegistrationNumber = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 4, 7));
    errors.push(patternValidator(data, /^[A-Z0-9]{4,7}$/));
    return errors.filter(err => err !== undefined);
}

const validateProductionYear = (data, min) => {
    let errors = [];
    if (!(/^\d+$/.test(data))) {
        errors.push("form.validation.year.chars")
    }

    errors.push(sizeValidator(data, 4, 4));

    let currentYear = new Date().getFullYear();
    if (data < 2010 || data > currentYear) {
        errors.push("form.validation.year.value")
    }
    return errors.filter(err => err !== undefined);
}

const validateVehicleBrand = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 20));
    errors.push(patternValidator(data, /^[a-zA-Z][a-zA-Z- ]+$/));
    return errors.filter(err => err !== undefined);
}

const validateVehicleModel = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 20));
    errors.push(patternValidator(data, /^[A-Za-z0-9-., ]+$/));
    return errors.filter(err => err !== undefined);
}

const validateLectureGroupName = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 4, 50));
    return errors.filter(err => err !== undefined);
}

const changeValidationMessages = (errors, identity) => {
    return errors.map(x => {
        const translationString = x.replace("field", "field." + identity.toLowerCase());
        const translated = i18n.t(translationString);
        return translated === translationString ? i18n.t(x) : translated;
    });
}

export const ValidatorType = {
    LOGIN: "LOGIN",
    EMAIL_ADDRESS: "EMAIL_ADDRESS",
    FIRSTNAME: "FIRSTNAME",
    LASTNAME: "LASTNAME",
    PASSWORD: "PASSWORD",
    PHONE_NUMBER: "PHONE_NUMBER",
    CONFIRMATION_CODE: "CONFIRMATION_CODE",
    LANGUAGE: "LANGUAGE",
    IMAGE: "IMAGE",
    COMMENT: "COMMENT",
    PRICE: "PRICE",
    VEHICLE_BRAND: "VEHICLE_BRAND",
    VEHICLE_MODEL: "VEHICLE_MODEL",
    COURSE_CATEGORY: "COURSE_CATEGORY",
    REGISTRATION_NUMBER: "REGISTRATION_NUMBER",
    PRODUCTION_YEAR: "PRODUCTION_YEAR",
    LECTURE_GROUP_NAME: "LECTURE_GROUP_NAME"
};

export const validatorFactory = (data, validatorType) => {
    switch (validatorType) {
        case ValidatorType.LOGIN:
            return changeValidationMessages(validateLogin(data), 'LOGIN');
        case ValidatorType.EMAIL_ADDRESS:
            return changeValidationMessages(validateEmailAddress(data), 'EMAIL_ADDRESS');
        case ValidatorType.FIRSTNAME:
            return changeValidationMessages(validateFirstname(data), 'FIRSTNAME');
        case ValidatorType.LASTNAME:
            return changeValidationMessages(validateLastname(data), 'LASTNAME');
        case ValidatorType.PASSWORD:
            return changeValidationMessages(validatePassword(data), 'PASSWORD');
        case ValidatorType.PHONE_NUMBER:
            return changeValidationMessages(validatePhoneNumber(data), 'PHONE_NUMBER');
        case ValidatorType.CONFIRMATION_CODE:
            return changeValidationMessages(validateConfirmationCode(data), 'CONFIRMATION_CODE');
        case ValidatorType.LANGUAGE:
            return changeValidationMessages(validateLanguage(data), 'LANGUAGE');
        case ValidatorType.IMAGE:
            return changeValidationMessages(validateImage(data), 'IMAGE');
        case ValidatorType.COMMENT:
            return changeValidationMessages(validateComment(data), 'COMMENT');
        case ValidatorType.PRICE:
            return changeValidationMessages(validatePrice(data), 'PRICE');
        case ValidatorType.VEHICLE_BRAND:
            return changeValidationMessages(validateVehicleBrand(data), 'VEHICLE_BRAND');
        case ValidatorType.VEHICLE_MODEL:
            return changeValidationMessages(validateVehicleModel(data), 'VEHICLE_MODEL');
        case ValidatorType.COURSE_CATEGORY:
            return changeValidationMessages(validateCourseCategory(data), 'COURSE_CATEGORY');
        case ValidatorType.REGISTRATION_NUMBER:
            return changeValidationMessages(validateRegistrationNumber(data), 'REGISTRATION_NUMBER');
        case ValidatorType.PRODUCTION_YEAR:
            return changeValidationMessages(validateProductionYear(data), 'PRODUCTION_YEAR');
        case ValidatorType.LECTURE_GROUP_NAME:
            return changeValidationMessages(validateLectureGroupName(data), 'LECTURE_GROUP_NAME');
        default:
            return [];
    }
}

