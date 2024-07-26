import React, { useState, useEffect } from "react";
import {
  ListItem,
  ListItemText,
  InputBase,
  Checkbox,
  ListItemSecondaryAction,
  IconButton,
} from "@mui/material";
import DeleteOutlined from "@mui/icons-material/DeleteOutlined";


// Todo 항목을 표시하고 관리하는 컴포넌트
const Todo = (props) => {
  const [item, setItem] = useState(props.item); // Todo 항목 상태 관리
  const [readOnly, setReadOnly] = useState(true);  // 입력 필드의 읽기 전용 상태 관리
  const deleteItem = props.deleteItem; // Todo 삭제 함수
  const editItem = props.editItem; // Todo 수정 함수
 

  // 입력 필드 값 변경 이벤트 핸들러
  const editEventHandler = (e) => {
    setItem({...item, title: e.target.value});  // 입력된 값으로 Todo 항목 업데이트
  };


  // 체크박스 상태 변경 이벤트 핸들러
  const checkboxEventHandler = (e) => {
    item.done = e.target.checked;  // 체크박스의 상태에 따라 Todo 완료 여부 업데이트
    editItem(item); // Todo 항목 수정 요청
  }

 // Todo 항목 삭제 이벤트 핸들러
 const deleteEventHandler = () => {
  deleteItem(item);  // Todo 삭제 요청
};

 // 입력 필드의 읽기 전용 상태 해제 이벤트 핸들러
const turnOffReadOnly = () => {
  setReadOnly(false); // 입력 필드를 편집 가능한 상태로 변경
}


   // 입력 필드의 읽기 전용 상태 복원 이벤트 핸들러
  const turnOnReadOnly = (e) => {
    if (e.key === "Enter" && readOnly === false) {
        setReadOnly(true); // Enter 키 입력 시 입력 필드를 읽기 전용 상태로 변경
        editItem(item); // 수정된 Todo 항목 저장 요청
    }
  };

  return (
    <ListItem>
      <Checkbox checked={item.done}
      onChange={checkboxEventHandler} />
      <ListItemText>
        <InputBase
          inputProps={{ 
              "aria-label": "naked",
              readOnly: readOnly }}
          onClick={turnOffReadOnly}
          onKeyDown={turnOnReadOnly}
          onChange={editEventHandler}
          type="text"
          id={item.id}
          name={item.id}
          value={item.title}
          multiline={true}
          fullWidth={true}
        />
      </ListItemText>
      <ListItemSecondaryAction>
        <IconButton aria-label="Delete Todo"
          onClick={deleteEventHandler} >
          <DeleteOutlined />
        </IconButton>
      </ListItemSecondaryAction>
    </ListItem>
  );
};

export default Todo;