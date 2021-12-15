<script lang="ts">
    import type { TextElementConfig } from "src/lib/models/page_elements/text_element_config";
    import { getValue } from "../../runtime/utils/value_receiver";

    export let config: TextElementConfig;
    export let page: string;

    let value = getValue(config.value, page);
</script>

<div>
    {#if typeof $value !== "undefined" && $value !== null}
        <p style="font-size: {config.options?.font_size ?? 'large'};">{($value).toString()}</p>
    {/if}
</div>
