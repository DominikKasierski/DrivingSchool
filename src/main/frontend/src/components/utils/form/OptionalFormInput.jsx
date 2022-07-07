import {useFormikContext, Field} from "formik"

function OptionalFormInput({name, placeholder, type, className = "col-6", errorClassname = "ml-4 text-danger mr-3"}) {
    const formik = useFormikContext();
    const errors = formik.errors[name]
    const touched = formik.touched[name]

    return (
        <div className={className}>
            <Field
                required={false}
                placeholder={placeholder}
                name={name}
                type={type}
                style={{width: "85%"}}
                className="d-inline-block form-control my-3"/>
            {errors && touched && errors.map((err, i) => {
                return (<div className={errorClassname}>{err}</div>)
            })}
        </div>
    )
}

export default OptionalFormInput;