<script lang="ts">
    import type {
        TextInputElementConfig,
        TextInputValidation,
    } from "src/lib/models/page_elements/text_input_element_config";
import { getValue } from "../../runtime/utils/value_receiver";
import { onMount } from "svelte";
    import { VariableRepository } from "../../runtime/respositories/variable_repository";

    

    export let config: TextInputElementConfig;
    export let page: string;

    onMount(() =>  {
        VariableRepository.instance.registerController(page, config.controller);
    });

    $: binding = getValue(config.controller.binding_variable, page);
</script>

<div>
    <input type=text bind:value={$binding}>
</div>
