<script lang="ts">
    import type { RowElementConfig } from "src/lib/models/page_elements/row_element_config";
import PageComposer from "../PageComposer.svelte";

    export let config: RowElementConfig;
    export let page: string;
</script>

<div
    class="flex flex-col md:flex-row {!config.cross_axis
        ? 'items-start'
        : `items-${config.cross_axis}`} {!config.main_axis
        ? 'justify-start'
        : `justify-${config.cross_axis}`}"
>
    <PageComposer pageName={page} pageElements={config.elements} />
</div>
