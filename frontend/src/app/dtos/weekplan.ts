export interface WeekPlanCreateDto {
    recipeBookId: number,
    startDate: Date,
    endDate: Date,
    weekdays: WeekDayDto[]
}

export interface WeekDayDto{
    weekday: String,
    dayTimes: String[]
}

export interface WeekDayEntity{
    weekday: Weekday,
    dayTimes: Daytime[]
}

export enum Weekday{
    Monday = "Montag",
    Tuesday = "Dienstag",
    Wednesday = "Mittwoch",
    Thursday = "Donnerstag",
    Friday = "Freitag",
    Saturday = "Samstag",
    Sunday = "Sonntag"
}

export enum Daytime{
    Breakfast = "Frühstück",
    Lunch = "Mittagessen",
    Dinner = "Abendessen"
}