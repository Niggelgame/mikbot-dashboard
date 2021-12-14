<script lang="ts">
    import { PageRepository } from "../runtime/respositories/page_repository";

    export let modulename: string;
    export let pagename: string;

    let pageConfig = PageRepository.instance.getPage(modulename, pagename);
</script>

{#await pageConfig}
    <div>
        <p>Loading {modulename}:{pagename}...</p>
    </div>
{:then config}
    <div>
        <p>{config.name}</p>
    </div>
{:catch err}
    <div>
        <p style="color: red">{err.message}</p>
    </div>
{/await}
