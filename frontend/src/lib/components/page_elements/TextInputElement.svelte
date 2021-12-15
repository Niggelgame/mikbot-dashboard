<script lang="ts">
    import type { TextInputElementConfig } from "src/lib/models/page_elements/text_input_element_config";

    export let config: TextInputElementConfig;
    export let page: string;
</script>

<div>
    <p>{config.type}</p>
    <p>{config.label}</p>
    <p>{page}</p>
</div>