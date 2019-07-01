<#--${str}-->
<#--<#list data as row>-->
<#--    ${row}-->
<#--</#list>-->

Feature: ${tags}  ${summary}

    # featureName: ${featureName}
    # url: ${url}
    Scenario: ${summary}
        <#if (paramList)??>

        <#list paramList as b>
        ${b.extra!''}
        </#list>

        * def params =
        """
        {
        <#list paramList as b>
            "${b.name!'no-name'}" = '#(${b.value!'no-value'})'<#if b_has_next>,</#if>
        </#list>
        }
        """

        </#if>

        <#if (pathFlag)>
        * def requestPath ='${url}'
        <#list pathList as pp>
        * replace requestPath.{${pp.name}} = params.${pp.value}
        </#list>
        Given url dwBaseUrl + requestPath
        </#if>
        <#if (!pathFlag)>
        Given url dwBaseUrl + '${url}'
        </#if>

        <#if (headerList)??>
        <#list headerList as q>
        ${q.extra!''}
        And header ${q.name!'def'} = params.${q.name}
        </#list>

        </#if>
        <#if (bodyFlag)>
        And request params
        </#if>
        <#if (queryList)??>
        <#list queryList as q>
        ${q.extra!''}
        And form field ${q.name!'def'} = params.${q.name}
        </#list>

        </#if>
        When method ${method}
        Then status 200


# response json demo:: ${responseJSON}