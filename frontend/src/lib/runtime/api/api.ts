import type { BaseConfig } from "src/lib/models/base_config";
import type { RequestVariableConfig } from "src/lib/models/variable_config";

export default class Api {
    private token: string = localStorage.getItem("token") || "";
    private static _instance: Api = new Api();

    // Use baseUrl() instead
    private _baseUrl?: string;

    constructor() {
        if (Api._instance)
            throw new Error("Use Singleton.instance");
            Api._instance = this;
    }

    static get instance() {
        return Api._instance;
    }

    private async baseUrl() : Promise<string>{
        const fallbaseUrl = "/dashboard/api";

        if (!this._baseUrl) {
            try {
                const result = await fetch("/config.json");
                if(!result.ok) {
                    console.warn("Could not fetch config.json. It is always recommended to create one. Look at https://github.com/niggelgame/mikbot-dashboard for more information.");
                    this._baseUrl = fallbaseUrl;
                } else {
                    const json = await result.json();
                    this._baseUrl = json["BASE_URL"] ?? fallbaseUrl;
                }

            } catch (error) {
                console.error(error);
                this._baseUrl = fallbaseUrl;
            }
        }
        return this._baseUrl;
    }

    setToken(token: string) {
        localStorage.setItem("token", token);
        token = localStorage.getItem("token");
    }

    async getConfig() : Promise<BaseConfig> {
        return fetch(`${await this.baseUrl()}/config`).then(res => res.json());
    }

    async variableFromRequestVariableConfig(variableConfig: RequestVariableConfig) : Promise<any> {
        return this.executeVariableRequest((await this.baseUrl()) + variableConfig.url, variableConfig.method, variableConfig.body, variableConfig.headers);
    }

    private executeVariableRequest(url: string, method: string = 'POST', body: any = {}, headers?: { [key: string]: string }) : Promise<any>{
        return fetch(url, this.getOptions(method, body, headers)).then(res => res.json());
    }

    async runAction(url:string, method: string, body?: any, headers?: { [key: string]: string }) : Promise<Response> {
        return await fetch((await this.baseUrl()) + url, this.getOptions(method, body, headers));
        // return this.executeRequest(url, method, body, headers);
    }

    private getOptions(method: string, body?: any, headers?: { [key: string]: string }) : RequestInit{
        return {
            method: method,
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