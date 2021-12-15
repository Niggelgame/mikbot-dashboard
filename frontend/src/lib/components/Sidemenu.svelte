<script>
    import { createEventDispatcher } from "svelte";
    import ModuleBrowser from "./ModuleBrowser.svelte";

    const dispatch = createEventDispatcher();

    let close = () => {
        dispatch("closemenu");
    };
</script>

<div
    class="h-full w-64 border-r overflow-auto border-gray-700 bg-gray-500 flex flex-col flex-nowrap justify-between text-white mx-auto"
>
    <div class="flex py-2 bg-gray-900 justify-center">
        <h3 class="text-xl px-2">Modules</h3>
        <div class="flex-1" />
        <button class="lg:hidden px-2" on:click={close}>X</button>
    </div>

    <ModuleBrowser on:close="{close}" />
    <div class="flex-1" />
</div>
