<script lang="ts">
    import { onDestroy } from "svelte";
    import type { Variable } from "../models/value_configs";
    import { getValue } from "../runtime/utils/value_receiver";

    let variable: Variable;
    let page: string;

    let varValue = getValue(variable, page);

    let internalValue: any = null;

    let error: any = null;

    const unsubscribe = varValue.subscribe((value) => {
        internalValue = value;
    });

    const errorUnsubscribe = varValue.onError((err) => {
        console.error(err);
        error = err;
    });

    onDestroy(() => {
        unsubscribe();
        errorUnsubscribe();
        varValue.dispose();
    });
</script>

{#if error != null}
    {#if $$slots["error"]}
        <slot name="error" />
    {:else}
        <div class="alert alert-danger">
            {error.message}
        </div>
    {/if}
{:else if internalValue === null}
    {#if $$slots["loading"]}
        <slot name="loading" />
    {:else}
        <p>Loading...</p>
    {/if}
{:else if $$slots["value"]}
    <slot name="value" value={internalValue} />
{:else}
    <p>{internalValue.toString()}</p>
{/if}
