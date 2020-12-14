INSERT INTO tb_notice (bid, cid, sid, write_date, writer, content, link, notice, title, viewcnt) VALUES(0, 0, 0, '', '', '', '', 0, '', 0);

select notice0_.sid as col_0_0_
     , notice0_.cid as col_1_0_
     , notice0_.bid as col_2_0_
     , notice0_.title as col_3_0_
     , notice0_.notice as col_4_0_
     , notice0_.viewcnt as col_5_0_
     , notice0_.content as col_6_0_
     , notice0_.filename as col_7_0_
     , notice0_.writer as col_8_0_
     , notice0_.write_date as col_9_0_ 
  from tb_notice notice0_ 
 inner join tb_userrole userrole1_ 
    on (notice0_.bid=userrole1_.bid) 
 where userrole1_.bid=1 
   and userrole1_.uid=0
   