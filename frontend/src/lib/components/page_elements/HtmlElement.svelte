<script lang="ts">
import type { HtmlInputElementConfig } from "src/lib/models/page_elements/html_input_element_config";
    export let config: HtmlInputElementConfig;
    export let page: string;
</script>

<p>{config.type}</p>
{@html config.raw_html}