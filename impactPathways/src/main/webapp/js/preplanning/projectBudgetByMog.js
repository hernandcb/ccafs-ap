// Global VARS
var $allBudgetInputs, $overallInputs, $CCAFSBudgetInputs;
var budgetByYear, genderBudgetByYear;
$(document).ready(init);

function init() {
  // Setting vars
  $percentageInputs = $("input.percentage");

  // Inputs per type
  $budgetInputs = $("input.budgetInput");
  $budgetCoFundedInputs = $("input.budgetCoFundedInput");
  $genderBudgetInputs = $("input.genderBudgetInput");
  $genderCoFundedBudgetInputs = $("input.genderCoFundedBudgetInput");

  // Remaining elements
  budgetByYear = new BudgetRemaining('#budgetByYear');
  coFundedBudgetByYear = new BudgetRemaining('#coFundedBudgetByYear');
  genderBudgetByYear = new BudgetRemaining('#genderBudgetByYear');
  coFundedGenderBudgetByYear = new BudgetRemaining('#coFundedGenderBudgetByYear');

  // Attach events
  attachEvents();

  // Active initial currency format to all inputs
  $percentageInputs.attr("autocomplete", "off").trigger("focusout");
  $budgetInputs.trigger("keyup");
  $genderBudgetInputs.trigger("keyup");
  $budgetCoFundedInputs.trigger("keyup");
  $genderCoFundedBudgetInputs.trigger("keyup");

  // Validate justification and information
  validateEvent('[name=save], [name=next]', [
    "#justification"
  ]);

  // Regenerating hash from form information
  setFormHash();
}

function attachEvents() {
  $percentageInputs.on("keydown", isNumber).on("focusout", setPercentage).on("focus", removePercentage).on("keyup",
      function(e) {
        isPercentage(e);
      }).on("click", function() {
    $(this).select();
  });

  addKeyUpEvent($budgetInputs, budgetByYear);
  addMouseEvent($budgetInputs, budgetByYear);

  addKeyUpEvent($genderBudgetInputs, genderBudgetByYear);
  addMouseEvent($genderBudgetInputs, genderBudgetByYear);

  addKeyUpEvent($budgetCoFundedInputs, coFundedBudgetByYear);
  addMouseEvent($budgetCoFundedInputs, coFundedBudgetByYear);

  addKeyUpEvent($genderCoFundedBudgetInputs, coFundedGenderBudgetByYear);
  addMouseEvent($genderCoFundedBudgetInputs, coFundedGenderBudgetByYear);

  $("form").submit(function(event) {
    $percentageInputs.each(function() {
      $(this).attr("readonly", true);
      $(this).val(removePercentageFormat($(this).val() || "0"));
    });
    return;
  });
}

function addMouseEvent(inputs,remaning) {
  $(inputs).on("mouseover", function(e) {
    $(inputs).parents('.budget').addClass('inputSelected');
    $(remaning.element).addClass('inputSelected');
  }).on("mouseout", function(e) {
    $(inputs).parents('.budget').removeClass('inputSelected');
    $(remaning.element).removeClass('inputSelected');
  });
}

function addKeyUpEvent(inputs,remaining) {
  $(inputs).on("keyup", function(e) {
    setPercentageCurrency($(this), remaining);
    checkPercentages($(e.target), $(inputs), remaining);
    calculateGenderAmount($(this).parents('.outputBudget'));
  });
}

function BudgetRemaining(budget) {
  this.element = $(budget).parents('.BudgetByYear');
  this.initValue = $(budget).find('input').val();
  this.setValue = function(value) {
    $(budget).find('span').text(setCurrencyFormat(value));
  };
  this.calculateRemain = function(percentage) {
    var result = (this.initValue / 100) * percentage;
    var value = this.initValue - result;
    this.setValue(Math.round(value * 100) / 100);

  };
  this.setError = function() {
    $(budget).addClass('fieldError');
  };
  this.removeError = function() {
    $(budget).removeClass('fieldError');
  };
}

function setPercentageCurrency(inputTarget,remainBudget) {
  var percentage = removePercentageFormat($(inputTarget).val() || "0");
  var value = (remainBudget.initValue / 100) * percentage;
  $(inputTarget).parents('.budget').find('span').text(setCurrencyFormat(value));
  // $(inputTarget).parents('.outputBudget').find('input.genderBudgetInput').trigger("keyup");
}

function calculateGenderAmount(outputBudget) {
  var $totalContribution = $(outputBudget).find('p.totalContribution');
  var $genderBudgetInput = $(outputBudget).find('p.genderContribution');

  var totalAmount = removeCurrencyFormat($totalContribution.find('span').text());
  var genderAmount = removeCurrencyFormat($genderBudgetInput.find('span').text());

  $totalContribution.removeClass('fieldError');
  $genderBudgetInput.removeClass('fieldError');
  $genderBudgetInput.parents('.budget').find('input').removeClass('fieldError');
  if(genderAmount > totalAmount) {
    $totalContribution.addClass('fieldError');
    $genderBudgetInput.addClass('fieldError');
    $genderBudgetInput.parents('.budget').find('input').addClass('fieldError');
  }
}

function checkPercentages(inputTarget,inputList,remainBudget) {
  var totalPercentage = 0;
  errorMessages = [];
  $(inputList).removeClass('fieldError');
  remainBudget.removeError();
  $(inputList).each(function(i,input) {
    totalPercentage += parseFloat(removePercentageFormat($(input).val() || "0"));
  });
  if(totalPercentage > 100) {
    errorMessages.push($('#budgetCanNotExcced').val());
    $(inputTarget).addClass('fieldError');
    remainBudget.setError();
  }
  remainBudget.calculateRemain(totalPercentage);

}

function setPercentage(event) {
  var $input = $(event.target);
  if($input.val().length == 0) {
    $input.val(0);
  }
  $input.val(setPercentageFormat($input.val()));
}

function removePercentage(event) {
  $input = $(event.target);
  $input.val(removePercentageFormat($input.val() || "0"));
}
