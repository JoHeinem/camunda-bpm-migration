package org.camunda.bpm.migration.examples;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.migration.plan.DeploymentSpec;
import org.camunda.bpm.migration.plan.MigrationPlan;
import org.camunda.bpm.migration.plan.ProcessDefinitionSpec;
import org.camunda.bpm.migration.plan.step.model.MigrationPlanFactory;
import org.camunda.bpm.migration.plan.step.model.ModelStep;
import org.camunda.bpm.migration.plan.step.variable.Conversion;
import org.camunda.bpm.migration.plan.step.variable.VariableDeleteStep;
import org.camunda.bpm.migration.plan.step.variable.VariableStep;
import org.camunda.bpm.migration.plan.step.variable.strategy.DeleteStrategy;
import org.camunda.bpm.migration.plan.step.variable.strategy.ReadConstantValue;
import org.camunda.bpm.migration.plan.step.variable.strategy.ReadStrategy;
import org.camunda.bpm.migration.plan.step.variable.strategy.WriteProcessVariable;
import org.camunda.bpm.migration.plan.step.variable.strategy.WriteStrategy;

public class Snippets {

  public void createMigrationPlan() {
    ProcessDefinitionSpec source = null;
    ProcessDefinitionSpec destination = null;

    MigrationPlan.builder().from(source).to(destination)
      .build();
  }

  public void specByDefinitionKeyAndVersionTag() {
    ProcessDefinitionSpec byDefinitionKeyAndVersionTag = ProcessDefinitionSpec.builder()
      .processDefinitionKey("myProcess")
      .versionTag("1")
      .build();
  }

  public void specByDefinitionKeyAndDeploymentDate() {
    DeploymentSpec year2015 = DeploymentSpec.builder()
      .earliestDeployment(ZonedDateTime.of(2015, 01, 01, 0, 0, 0, 0, ZoneId.of("CET")))
      .latestDeployment(ZonedDateTime.of(2015, 12, 31, 23, 59, 59, 0, ZoneId.of("CET")))
      .build();

    ProcessDefinitionSpec byDefinitionKeyAndDeploymentDate = ProcessDefinitionSpec.builder()
      .processDefinitionKey("myProcess")
      .deploymentSpec(year2015)
      .build();
  }

  public void createMappingStep() {
    RuntimeService runtimeService = null;

    //please note that this time we are(!) using Camunda's MigrationPlan
    //The MigrationPlan.build() method checks the existence of the source and target ProcessDefinitions,
    //that's why only a function for creation is provided and not the MigrationPlan itself
    MigrationPlanFactory camundaMigrationPlan =
      (source, target) -> runtimeService
        .createMigrationPlan(source, target)
        .mapEqualActivities()
        .build();  //could fail on startup!
    ModelStep mappingStep = new ModelStep(camundaMigrationPlan);
  }

  public void createVariableStep() {
    ReadStrategy readStrategy = null;
    WriteStrategy writeStrategy = null;
    String sourceVariableName = null;
    String targetVariableName = null;
    Conversion conversion = null;

    VariableStep variableStep = VariableStep.builder()
      .readStrategy(readStrategy)
      .writeStrategy(writeStrategy)
      .sourceVariableName(sourceVariableName)
      .targetVariableName(targetVariableName)
      .conversion(conversion)
      .build();
  }

  public void renameVariable() {
    ReadStrategy readStrategy = null;
    WriteStrategy writeStrategy = null;
    String sourceVariableName = null;
    String targetVariableName = null;

    VariableStep variableStep = VariableStep.builder()
      .readStrategy(readStrategy)
      .writeStrategy(writeStrategy)
      .sourceVariableName(sourceVariableName)
      .targetVariableName(targetVariableName)
      .build();
  }

  public void changeVariableType() {
    ReadStrategy readStrategy = null;
    WriteStrategy writeStrategy = null;
    String sourceVariableName = null;

    Conversion conversionFunction = (TypedValue originalTypedValue) -> {
      String invoiceNumberWithoutDelimiters = originalTypedValue.getValue().toString().replace("-", "");
      return Variables.longValue(Long.valueOf(invoiceNumberWithoutDelimiters));
    };

    VariableStep variableStep = VariableStep.builder()
      .readStrategy(readStrategy)
      .writeStrategy(writeStrategy)
      .sourceVariableName(sourceVariableName)
      .conversion(conversionFunction)
      .build();
  }

  public void createNewVariable() {
    ReadStrategy constantValue = new ReadConstantValue(Variables.integerValue(42));
    WriteStrategy writeStrategy = new WriteProcessVariable();

    VariableStep variableStep = VariableStep.builder()
      .readStrategy(constantValue)
      .writeStrategy(writeStrategy)
      .sourceVariableName("theAnswer")
      .build();
  }

  public void deleteVariable() {
    DeleteStrategy deleteStrategy = null;
    String variableName = null;

    VariableDeleteStep variableDeleteStep = VariableDeleteStep.builder()
      .deleteStrategy(deleteStrategy)
      .variableName(variableName)
      .build();
  }
}
