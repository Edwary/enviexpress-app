export interface ApiResponse<T> {
    statusCodeValue : number;
    statusCode : string;
    headers : JSON;
    body : T;
}