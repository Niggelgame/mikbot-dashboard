import type { BaseConfig } from "src/lib/models/base_config";
import type { ModuleConfig } from "src/lib/models/module_config";
import type { PageConfig } from "src/lib/models/page_config";
import Api from "../api/api";

class ConfigStore {
    private config?: BaseConfig;

    public loading: boolean = true;

    private promise: Promise<void>;

    constructor() {
        this.promise = new Promise(async (resolve, reject) => {
            try {
                const config = await Api.instance.getConfig();
                this.config = config;
                this.loading = false;
                resolve();
            } catch (e) {
                reject(e);
            }
        });
    }

    public async  getConfig(): Promise<BaseConfig> {
        await this.promise;
        return this.config;
    }
}

export class PageRepository {
    private static _instance: PageRepository = new PageRepository();
    private configStore: ConfigStore = new ConfigStore();

    constructor() {
        if (PageRepository._instance)
            throw new Error("Use Singleton.instance");
        PageRepository._instance = this;
    }

    static get instance() {
        return PageRepository._instance;
    }

    async getPage(module: string, page: string): Promise<PageConfig> {
        const config = await this.configStore.getConfig();
        const moduleConfig = config.modules.find((p) => p.name === module);
        const pageConfig = moduleConfig?.pages.find((p) => p.name === page);
        if (!pageConfig)
            throw new Error(`Page ${module}/${page} not found`);
        return pageConfig;
    }

    async getModules() : Promise<ModuleConfig[]> {
        const config = await this.configStore.getConfig();
        return config.modules;
    }
}