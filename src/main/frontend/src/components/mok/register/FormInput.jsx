import {useFormikContext, Field} from "formik"

function FormInput({name, placeholder, type}) {
    const formik = useFormikContext();
    const errors = formik.errors[name]
    const touched = formik.touched[name]

    return (
        <div className={"col-6"}>
            <div className={"d-inline-block ml-4 mr-3"}>*</div>
            <Field
                required={true}
                placeholder={placeholder}
                name={name}
                type={type}
                className="d-inline-block form-control my-3 w-75"/>
            {errors && touched && errors.map((err, i) => {
                return (<div className={"ml-5 text-danger"}>{err}</div>)
            })}
        </div>
    )
}

export default FormInput;