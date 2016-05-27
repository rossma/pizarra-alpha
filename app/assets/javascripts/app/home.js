define(function () {
  $('table.phrase-tbl > tbody > tr').click(function() {
    window.location.href = jsRoutes.controllers.HomeController.edit($(this).data("id")).url;
  });
});