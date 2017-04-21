package org.camunda.bpm.migration.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessDefinitionSpec {
  private String processDefinitionKey;
  private String versionTag;
  private Integer processDefinitionVersion;
  private DeploymentSpec deploymentSpec;
}
