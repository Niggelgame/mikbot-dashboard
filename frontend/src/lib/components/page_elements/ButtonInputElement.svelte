<script lang="ts">
    import type { ButtonInputElementConfig } from "src/lib/models/page_elements/button_input_element_config";
    import { runAction } from "../../runtime/utils/action_executor";
    import { getColor, getDarkerColor } from "../../runtime/utils/colors";

    export let config: ButtonInputElementConfig;
    export let page: string;

    $: color = getColor(config.options?.color ?? "blue");
    $: hoverColor = getDarkerColor(config.options?.color ?? "blue");
    $: activeColor = getDarkerColor(config.options?.color ?? "blue", 2);

    let enabled = false;

    let onClick = async (e: MouseEvent) => {
        if (enabled) {
            enabled = false;
            await runAction(config.action, page);
            enabled = true;
        }
    };
</script>

<!-- <div>
    <p>{config.type}</p>
    <p class="">Label: {config.label}</p>
    <p>Page {page}</p>
</div> -->
<button
    style="--color: {color}; --hoverColor: {hoverColor}; --activeColor: {activeColor}; 
        {!config.options.height
        ? 'height: auto;'
        : `height: ${config.options.height};`} {!config.options.width
        ? 'width: auto;'
        : `width: ${config.options.width};`}"
    class="rounded  text-white   inline-flex items-center px-4 py-2 font-semibold leading-6 text-sm shadow hover:transition-all"
    on:click={onClick}
>
    {#if !enabled}
        <svg
            class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
        >
            <circle
                class="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                stroke-width="4"
            />
            <path
                class="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
            />
        </svg>
    {/if}
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
