<script lang="ts">
    import type { SizedBoxElementConfig } from "src/lib/models/page_elements/sized_box_element_config";

    export let config: SizedBoxElementConfig;
    export let page: string;
</script>


<div style="{!config.height
    ? 'height: auto;'
    : `height: ${config.height};`} {!config.width
    ? 'width: auto;'
    : `width: ${config.width};`}"></div>