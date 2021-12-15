<script lang="ts">
    import type { ButtonInputElementConfig } from "src/lib/models/page_elements/button_input_element_config";
    import { runAction } from "../../runtime/utils/action_executor";
    import { getColor, getDarkerColor } from "../../runtime/utils/colors";

    export let config: ButtonInputElementConfig;
    export let page: string;

    $: color = getColor(config.options?.color ?? "blue");
    $: hoverColor = getDarkerColor(config.options?.color ?? "blue");
    $: activeColor = getDarkerColor(config.options?.color ?? "blue", 2);

    let onClick = (e: MouseEvent) => {
        console.log("Executing onClick");
        runAction(config.action, page);
    };
</script>

<div>
    <p>{config.type}</p>
    <p class="">Label: {config.label}</p>
    <p>Page {page}</p>
</div>
<button
    style="--color: {color}; --hoverColor: {hoverColor}; --activeColor: {activeColor}; 
        {!config.options.height
        ? 'height: auto;'
        : `height: ${config.options.height};`} {!config.options.width
        ? 'width: auto;'
        : `width: ${config.options.width};`}"
    class="inline-block rounded py-1 px-3  text-white  hover:transition-all"
    on:click={onClick}
>
    <p>{config.label}</p>
</button>

<style>
    button {
        background-color: var(--color);
    }
    button:hover {
        background-color: var(--hoverColor);
    }
    button:active {
        background-color: var(--activeColor);
    }
</style>
