import type { BaseConfig } from "src/lib/models/base_config";
import type { RequestVariableConfig } from "src/lib/models/variable_config";

export default class Api {
    private token: string = localStorage.getItem("token") || "";
    private static _instance: Api = new Api();

    constructor() {
        if (Api._instance)
            throw new Error("Use Singleton.instance");
            Api._instance = this;
    }

    static get instance() {
        return Api._instance;
    }

    setToken(token: string) {
        localStorage.setItem("token", token);
        token = localStorage.getItem("token");
    }

    getConfig() : Promise<BaseConfig> {
        return fetch("/assets/sample.json").then(res => res.json());
    }

    variableFromRequestVariableConfig(variableConfig: RequestVariableConfig) : Promise<any> {
        return this.executeVariableRequest(variableConfig.url, variableConfig.method, variableConfig.body, variableConfig.headers);
    }

    private executeVariableRequest(url: string, method: string = 'GET', body?: any, headers?: { [key: string]: string }) : Promise<any>{
        return fetch(url, this.getOptions(method, body, headers)).then(res => res.json());
    }

    private getOptions(method: string, body?: any, headers?: { [key: string]: string }) : RequestInit{
        return {
            method: method,
            mode: 'no-cors',
            cache: 'no-cache',
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`,
                ...headers
            },
            redirect: 'follow',
            referrerPolicy: 'no-referrer',
            body: body ? JSON.stringify(body) : null
        };
    }

}