<script lang="ts">
    import { createEventDispatcher } from "svelte";
    import { navigate } from "svelte-routing";

    import { PageRepository } from "../runtime/respositories/page_repository";
    import ModuleButton from "./ModuleButton.svelte";

    const dispatch = createEventDispatcher();

    let modules = PageRepository.instance.getModules();

    let selectPage = (e) => {
        const mod = e.detail.module;
        const page = e.detail.page;
        // Use encodeURIComponent to escape the page name (e. g. allow  "?", "&", "/", "=" and spaces)
        navigate(`/${encodeURIComponent(mod)}/${encodeURIComponent(page)}`);
        setTimeout(()=> {
            dispatch("close");
        }, 100);
    };
</script>

{#await modules}
    <div>
        <p class="">Loading...</p>
    </div>
{:then modules}
    {#each modules as mod, i}
        {#if mod.pages.length == 1}
            <ModuleButton
                module={mod.name}
                page={`${mod.pages[0].name}`}
                indented={false}
                on:onclick={selectPage}
            />
        {:else}
            <ModuleButton
                module={mod.name}
                page={mod.name}
                active={false}
                indented={false}
            />
            {#each mod.pages as page, i}
                <ModuleButton
                    module={mod.name}
                    page={page.name}
                    on:onclick={selectPage}
                />
            {/each}
        {/if}
    {/each}
{:catch error}
    <div>
        <p class="">Error loading modules</p>
    </div>
{/await}
