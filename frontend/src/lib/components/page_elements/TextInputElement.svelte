<script lang="ts">
    import type {
        TextInputElementConfig,
        TextInputValidation,
    } from "src/lib/models/page_elements/text_input_element_config";
    import { VariableRepository } from "../../runtime/respositories/variable_repository";

    export let config: TextInputElementConfig;
    export let page: string;

    let requiredValidator: TextInputValidation[];

    $: {
        if (config.required) {
            requiredValidator = [{ type: "regex", value: "^(?!s*$).+" }];
        } else {
            requiredValidator = [];
        }
    }
    $: inputvalue = VariableRepository.instance.getVariableBinding(
        page,
        config.input_identifier,
        null,
        [...config.input_validation, ...requiredValidator]
    );

    $: inputbinding = inputvalue.value;


</script>

<div>
    <input type=text bind:value={$inputbinding}>
</div>
