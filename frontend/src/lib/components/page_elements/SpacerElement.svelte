<script lang="ts">
    import type { SpacerElementConfig } from "src/lib/models/page_elements/spacer_element_config";
    import PageComposer from "../PageComposer.svelte";

    export let config: SpacerElementConfig;
    export let page: string;
</script>

<div class="flex-{config.flex ?? 1}">
    {#if config.child}
        <PageComposer pageName="{page}" pageElements="{[config.child]}" />
    {/if}
</div>
