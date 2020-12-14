package com.test.spot.data.event;

import com.test.spot.data.MatchFill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchOrderFillInstruction {

    private String symbol;

    private List<MatchFill> fillList;

}
