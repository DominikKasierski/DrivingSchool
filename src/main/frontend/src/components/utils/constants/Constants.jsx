export const rolesConstant = {
    admin: 'ADMIN',
    instructor: 'INSTRUCTOR',
    trainee: 'TRAINEE',
};

export const permissionsConstant = {
    A: 'A',
    B: 'B',
    C: 'C',
};

export const notificationType = {
    DANGER: "danger",
    WARNING: "warning",
    SUCCESS: "success",
}

export const notificationDuration = {
    INFINITY: -1,
    LONG: 60_000,
    STANDARD: 10_000,
    SHORT: 5_000,
}

export const paymentStatus = {
    CANCELLED: 'CANCELLED',
    IN_PROGRESS: 'IN_PROGRESS',
    CONFIRMED: 'CONFIRMED',
    REJECTED: 'REJECTED',
};

export const REFRESH_TIME = 60 * 1000