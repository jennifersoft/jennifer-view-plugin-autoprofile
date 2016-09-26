var combo_domain, table_detail;

jui.ready([ "ui.combo", "ui.datepicker", "chart.builder", "grid.table" ], function(combo, datepicker, builder, table) {
    combo_domain = combo("#combo_domain", {
        width: 150,
        event: {
            change: function(data) {
                updateAutoProfileList();
            }
        }
    });

    table_detail = table("#table_detail", {
        fields: [
            null,
            "name",
            "baselineTransactionElapsedTimeForProfile",
            "baselineMethodElapsedTimeForProfile",
            "baselineIgnorableMethodElapsedTimeForSendToDataServer",
            "profileQueueSize",
            "profileClearInterval",
            "checkIntervalForSelectTransactionToProfile",
            "methodSamplingIntervalDuringProfile",
            "methodSamplingStackDepthLimit",
            "maxProfileTargetCount",
            "enable",
            null
        ],
        resize: true,
        editRow: [ 2, 3, 4, 5, 6, 7, 8, 9, 10 ],
        sort: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ],
        scrollHeight: $(window).height() - 150,
        tpl: {
            row: $("#tpl_row").html(),
            none: $("#tpl_none").html()
        },
        event: {
            sort: setSortEff,
            editstart: function(row) {
                $(row.element).find("td.enable").html("<input type='checkbox' " + ((row.data.enable) ? "checked" : "") + "/>");
            },
            editend: function(row) {
                row.data.enable = $(row.element).find("input[type=checkbox]")[0].checked;

                $.get("/plugin/autoprofile/put", $.extend({
                    sid: combo_domain.getValue()
                }, row.data), function() {
                    updateAutoProfileList();
                });

                return true;
            }
        }
    });

    updateAutoProfileList();
});

function updateAutoProfileList() {
    $.get("/plugin/autoprofile/list", {
        sid: combo_domain.getValue()
    }, function(list) {
        table_detail.update(list);
    });
}

function removeAutoProfile(oid) {
    $.get("/plugin/autoprofile/remove", {
        sid: combo_domain.getValue(),
        oid: oid
    }, function() {
        updateAutoProfileList();
    });
}

function restoreAutoProfile(oid) {
    $.get("/plugin/autoprofile/restore", {
        sid: combo_domain.getValue(),
        oid: oid
    }, function() {
        updateAutoProfileList();
    });
}