<script lang="ts">
    import type { ImageElementConfig } from "src/lib/models/page_elements/image_element_config";

    export let config: ImageElementConfig;
    export let page: string;
</script>

<div>
    <figure>
        <img
            src={config.url}
            alt={config.alt ?? `unknown image content on ${page}`}
            style="{!config.options.height
                ? 'height: auto;'
                : `height: ${config.options.height};`} {!config.options.width
                ? 'width: auto;'
                : `width: ${config.options.width};`}"
        />
        {#if config.options.caption}
            <figcaption>{config.options.caption}</figcaption>
        {/if}
    </figure>
</div>
