import {useFormikContext, Field} from "formik"

function FormInput({name, placeholder, type, className = "col-6"}) {
    const formik = useFormikContext();
    const errors = formik.errors[name]
    const touched = formik.touched[name]

    return (
        <div className={className}>
            <div className={"d-inline-block mx-2"}>*</div>
            <Field
                required={true}
                placeholder={placeholder}
                name={name}
                type={type}
                style={{width: "85%"}}
                className="d-inline-block form-control my-3"/>
            {errors && touched && errors.map((err, i) => {
                return (<div className={"ml-4 text-danger"}>{err}</div>)
            })}
        </div>
    )
}

export default FormInput;