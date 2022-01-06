export interface ActionResponse {
    data: string,
    variable_updates: any
} 

interface VariableUpdate {
    value: any,
    path?: string[]
}