import i18n from "i18next";
import {reactI18nextModule} from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import enTranslation from "./locales/en/translation.json";
import plTranslation from "./locales/pl/translation.json";
import {enDays, enMonths} from "./locales/en/date";
import {plDays, plMonths} from "./locales/pl/date";

i18n
    .use(reactI18nextModule)
    .use(LanguageDetector)
    .init({
        resources: {
            en: {
                translation: enTranslation,
            }, pl: {
                translation: plTranslation,
            },
        }, fallbackLng: 'en', interpolation: {
            escapeValue: false,
        },
    });

export const dateConverter = (date) => {
    const parsedDate = new Date(date)
    const year = parsedDate.getFullYear()
    const day = parsedDate.getDate()
    let hours = parsedDate.getHours()
    let minutes = parsedDate.getMinutes()
    let seconds = parsedDate.getSeconds()

    if (hours < 10) {
        hours = `0${hours}`
    }
    if (minutes < 10) {
        minutes = `0${minutes}`
    }
    if (seconds < 10) {
        seconds = `0${seconds}`
    }

    const time = `${hours}:${minutes}:${seconds}`

    if (i18n.language === 'en') {
        const month = enMonths[parsedDate.getMonth()]
        const weekDay = enDays[parsedDate.getDay()]
        return `${weekDay}, ${day} ${month} ${year} ${time}`
    } else {
        const month = plMonths[parsedDate.getMonth()]
        const weekDay = plDays[parsedDate.getDay()]
        return `${weekDay}, ${day} ${month} ${year} ${time}`
    }
}

export default i18n;
