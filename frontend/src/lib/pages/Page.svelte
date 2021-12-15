<script lang="ts">
    import PageComposer from "../components/PageComposer.svelte";
    import type { PageConfig } from "../models/page_config";

    import { PageRepository } from "../runtime/respositories/page_repository";
    import { VariableRepository } from "../runtime/respositories/variable_repository";

    export let modulename: string;
    export let pagename: string;
    export let location: string;

    // Auto reload PageConfig on prop change / route change
    $: pageConfig = PageRepository.instance.getPage(modulename, pagename);
    $: (async (pageConfig: Promise<PageConfig>) => {
        console.log("PageConfig loaded");
        VariableRepository.instance.loadVariablesForPage(
            await pageConfig,
            modulename
        );
    })(pageConfig);
</script>

{#await pageConfig}
    <div>
        <p>Loading {modulename}:{pagename}...</p>
    </div>
{:then config}
    <div>
        <p>{config.name}</p>
    </div>
    <PageComposer
        pageElements={config.elements}
        pageName={`${modulename}/${pagename}`}
    />
{:catch err}
    <div>
        <p style="color: red">{err.message}</p>
    </div>
{/await}
