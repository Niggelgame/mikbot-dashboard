<script lang="ts">
    import { BUTTON_ELEMENT_TYPE } from "../models/page_elements/button_input_element_config";
    import { COLUMN_ELEMENT_TYPE } from "../models/page_elements/column_element_config";
    import { HTML_INPUT_ELEMENT_TYPE } from "../models/page_elements/html_input_element_config";
    import { IMAGE_ELEMENT_TYPE } from "../models/page_elements/image_element_config";
    import { ROW_ELEMENT_TYPE } from "../models/page_elements/row_element_config";
import { SIZED_BOX_ELEMENT_TYPE } from "../models/page_elements/sized_box_element_config";
    import { SPACER_ELEMENT_TYPE } from "../models/page_elements/spacer_element_config";
    import { TEXT_ELEMENT_TYPE } from "../models/page_elements/text_element_config";
    import { TEXT_INPUT_ELEMENT_TYPE } from "../models/page_elements/text_input_element_config";

    import type { PageElementConfig } from "../models/page_element_config";

    import ButtonInputElement from "./page_elements/ButtonInputElement.svelte";
    import ColumnElement from "./page_elements/ColumnElement.svelte";
    import HtmlElement from "./page_elements/HtmlElement.svelte";
    import ImageElement from "./page_elements/ImageElement.svelte";
    import RowElement from "./page_elements/RowElement.svelte";
    import SpacerElement from "./page_elements/SpacerElement.svelte";
    import TextElement from "./page_elements/TextElement.svelte";
    import TextInputElement from "./page_elements/TextInputElement.svelte";
    import SizedBoxElement from "./page_elements/SizedBoxElement.svelte";

    export let pageElements: PageElementConfig[];
    export let pageName: string;

    let getComponentForElement = (pageElement: PageElementConfig) => {
        if (pageElement.type === BUTTON_ELEMENT_TYPE) {
            return ButtonInputElement;
        } else if (pageElement.type === COLUMN_ELEMENT_TYPE) {
            return ColumnElement;
        } else if (pageElement.type === HTML_INPUT_ELEMENT_TYPE) {
            return HtmlElement;
        } else if (pageElement.type === IMAGE_ELEMENT_TYPE) {
            return ImageElement;
        } else if (pageElement.type === ROW_ELEMENT_TYPE) {
            return RowElement;
        } else if (pageElement.type === SIZED_BOX_ELEMENT_TYPE) {
            return SizedBoxElement;
        } else if (pageElement.type === SPACER_ELEMENT_TYPE) {
            return SpacerElement;
        } else if (pageElement.type === TEXT_ELEMENT_TYPE) {
            return TextElement;
        } else if (pageElement.type === TEXT_INPUT_ELEMENT_TYPE) {
            return TextInputElement;
        }
    };
</script>

{#each pageElements as pageElement}
    <svelte:component
        this={getComponentForElement(pageElement)}
        config={pageElement}
        page={pageName}
    />
    <br />
{/each}
