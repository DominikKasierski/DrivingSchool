import React from 'react';
import i18n from "../../../i18n";
import DatePicker from "react-datepicker";
import pl from "date-fns/locale/pl";
import enNz from "date-fns/locale/en-NZ";
import moment from "moment";

function CustomDatePicker({pickDate, setPickDate, setEndDate, label, minDate, currentEndDate, report = false, className= "date-picker-custom"}) {
    const handleDateChange = (date) => {
        if (currentEndDate && moment(date).isAfter(currentEndDate)) {
            setEndDate(date);
        }
        setPickDate(date);
    }

    return (
        <div className={className}>
            {report ? <h5>{label}</h5> : <span className="font-weight-bold">{label}</span>}

            <DatePicker locale={i18n.language === "pl" ? pl : enNz} selected={pickDate}
                        onChange={handleDateChange}
                        className={"text-center"}
                        dateFormat="Pp"
                        minDate={minDate}
                        showTimeSelect
                        timeCaption={i18n.t("generate.report.time")}/>
        </div>
    );
}

export default CustomDatePicker;