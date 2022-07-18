import TimeTable from 'react-timetable-events'

export default function CustomTimetable({mondayEvents, tuesdayEvents, wednesdayEvents, thursdayEvents, fridayEvents}) {
    return (
        <TimeTable
            events={{
                monday: [mondayEvents,],
                tuesday: [tuesdayEvents],
                wednesday: [wednesdayEvents],
                thursday: [thursdayEvents],
                friday: [fridayEvents],
            }}
        />
    )
}